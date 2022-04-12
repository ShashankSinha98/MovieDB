package com.shashank.moviedb.util;

// CacheObject: Type for resource data (db cache)
// RequestObject: Type for API response (network request)

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.remote.ApiResponse;

public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private static final String TAG = "NetworkBoundResource";
    private AppExecutors appExecutors;

    // Single source of data
    private MediatorLiveData<Resource<CacheObject>> results;


    // called to get cached data from db
    protected abstract LiveData<CacheObject> loadFromDB();

    // Called with data in db to decide whether to fetch
    // potentially updated data from network
    /*** Eg- Suppose Movie data is last updated 1 week ago, and app sync
     Movie data which is > 1 week old ***/
    protected abstract Boolean shouldFetch(CacheObject data);

    // Called to create API call
    /*** Note- We will convert Retrofit Call to LiveData
     // No need to use executors for background operation for network ***/
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();


    // called to save the results of API response into db
    protected abstract void saveCallResult(RequestObject item);


    // Return a LiveData object that represents the resource that's implemented
    // in the base class
    /*** Return data to UI - single source of truth - always cache data ***/
    public LiveData<Resource<CacheObject>> getAsLiveData() {
     return results;
    }


    public NetworkBoundResource(AppExecutors appExecutors) {
        ELog.d(TAG,"NetworkBoundResource constructor");
        this.appExecutors = appExecutors;
        results = new MediatorLiveData<>();

        // update livedata for loading status
        ELog.d(TAG,"1. Update livedata for loading status");
        results.setValue((Resource<CacheObject>) Resource.loading(null));

        // observe livedata source from local db
        ELog.d(TAG,"2. observe livedata source from local db");
        LiveData<CacheObject> dbSource = loadFromDB();

        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                results.removeSource(dbSource); // stop observing this data source

                if(shouldFetch(cacheObject)) {
                    ELog.d(TAG, "3.1 fetch from network");
                    // get data from network
                    fetchFromNetwork(dbSource);
                } else {
                    ELog.d(TAG, "3.2 fetch from db only");
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject cacheObject) {
                            setValue((Resource<CacheObject>) Resource.success(cacheObject));
                        }
                    });
                }
            }
        });
    }

    // fetch data from network and insert it into db cache
    /**
     1) Observe local db
     2) if <condition/> query the network
     3) Stop observing local db
     4) Insert new data into local db
     5) begin observing local db again to see refreshed data from network
     */
    private void fetchFromNetwork(LiveData<CacheObject> dbSource) {
        ELog.d(TAG, "fetchFromNetwork called");

        // update live data for loading status. show cached data meanwhile
        ELog.d(TAG,"update live data for loading status. show cached data meanwhile");
        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                setValue(Resource.loading(cacheObject));
            }
        });


        // response from retrofit in form of LiveData (not call)
        ELog.d(TAG,"getting response from retrofit in form of LiveData");
        LiveData<ApiResponse<RequestObject>> apiResponse = createCall();
        ELog.d(TAG,"response from retrofit in form of LiveData received 1");


        results.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(ApiResponse<RequestObject> requestObjectApiResponse) {
                results.removeSource(dbSource);
                results.removeSource(apiResponse);

                /**
                 *  3 cases -
                 *  1) ApiSuccessResponse
                 *  2) ApiEmptyResponse
                 *  3) ApiErrorResponse
                 * */
                ELog.d(TAG,"response from retrofit in form of LiveData received 2");

                if(requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse) {
                    ELog.d(TAG, "onChanged: ApiSuccessResponse");

                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            // save response to local db - need to be done on background thread, so used diskIO
                            // saveCallResult(processResponse(requestObjectApiResponse))
                            ELog.d(TAG,"requestObjectApiResponse.getBody(): "+((ApiResponse.ApiSuccessResponse)requestObjectApiResponse).getBody());
                            saveCallResult((RequestObject) ((ApiResponse.ApiSuccessResponse)requestObjectApiResponse).getBody());

                            // pass result to our single source data i.e., results
                            ELog.d(TAG,"appExecutors.mainThread().execute called");
                            appExecutors.mainThread().execute(new Runnable() { // used main thread as we need to SET value to our single source data i.e., results
                                @Override
                                public void run() {
                                    // reading latest data from db
                                    ELog.d(TAG,"reading latest data from db");
                                    results.addSource(loadFromDB(), new Observer<CacheObject>() {
                                        @Override
                                        public void onChanged(CacheObject cacheObject) {
                                            ELog.d(TAG,"pass result to our single source data i.e., results");
                                            setValue(Resource.success(cacheObject));
                                        }
                                    });
                                }
                            });
                        }
                    });
                }

                else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse) {
                    ELog.d(TAG, "onChanged: ApiEmptyResponse");

                    // get data from local and set it to our single source data i.e., results
                    // Also notify that it was error response
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            // used main thread as we need to SET value to our single source data i.e., results
                            // As we didn't update db as response has error, get old data from db
                            results.addSource(loadFromDB(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(CacheObject cacheObject) {
                                    setValue(Resource.error(
                                            ((ApiResponse.ApiErrorResponse)requestObjectApiResponse).getErrorMessage(),
                                            cacheObject));
                                }
                            });
                        }
                    });
                }



                else if(requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse) {
                    ELog.d(TAG, "onChanged: ApiEmptyResponse");
                    // get data from local and set it to our single source data i.e., results
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() { // used main thread as we need to SET value to our single source data i.e., results
                            // As we didn't update db as response was empty, get old data from db
                            results.addSource(loadFromDB(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(CacheObject cacheObject) {
                                    setValue(Resource.success(cacheObject));
                                }
                            });
                        }
                    });
                }
            }
        });

    }


    private void setValue(Resource<CacheObject> newValue) {
        ELog.d(TAG,"set value called");
        if(results.getValue()!=newValue) {
            ELog.d(TAG,"new value set: "+newValue.getStatus());
            results.setValue(newValue);
        } else {
            ELog.d(TAG,"new value not set, same as previous: "+results.getValue());
        }
    }



}
