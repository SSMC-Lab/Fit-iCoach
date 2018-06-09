    # Copyright (C) 2009 The Android Open Source Project
    #
    # Licensed under the Apache License, Version 2.0 (the "License");
    # you may not use this file except in compliance with the License.
    # You may obtain a copy of the License at
    #
    #      http://www.apache.org/licenses/LICENSE-2.0
    #
    # Unless required by applicable law or agreed to in writing, software
    # distributed under the License is distributed on an "AS IS" BASIS,
    # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    # See the License for the specific language governing permissions and
    # limitations under the License.
    #
    LOCAL_PATH := $(call my-dir)

    include $(CLEAR_VARS)

    LOCAL_MODULE    := jni-dtw
    LOCAL_SRC_FILES := dtw.cpp
    LOCAL_SRC_FILES += analysis.cpp
    LOCAL_SRC_FILES += dtw_Impl.cpp
    LOCAL_SRC_FILES += dtw_Impl.h

    LOCAL_SRC_FILES +=emd.h
    LOCAL_SRC_FILES +=emd.cpp
    LOCAL_SRC_FILES +=emd_interface.h
    LOCAL_SRC_FILES +=emd_interface.cpp

    LOCAL_SRC_FILES += test.cpp
        LOCAL_SRC_FILES += test.h
        LOCAL_SRC_FILES += WarpPath.cpp
        LOCAL_SRC_FILES +=WarpPath.h
         LOCAL_SRC_FILES +=BinaryDistance.h
          LOCAL_SRC_FILES +=ColMajorCell.cpp
           LOCAL_SRC_FILES +=ColMajorCell.h
            LOCAL_SRC_FILES +=CostMatrix.h
             LOCAL_SRC_FILES +=DTW.h
              LOCAL_SRC_FILES +=EuclideanDistance.h
               LOCAL_SRC_FILES +=ExpandedResWindow.h
                LOCAL_SRC_FILES +=FastDTW.cpp
                 LOCAL_SRC_FILES +=FastDTW.h
                  LOCAL_SRC_FILES +=FDAssert.h
                   LOCAL_SRC_FILES +=FDMath.h
                    LOCAL_SRC_FILES +=Foundation.h
                     LOCAL_SRC_FILES +=FullWindow.h
                      LOCAL_SRC_FILES +=JavaTypes.h
                       LOCAL_SRC_FILES +=LinearWindow.h
                        LOCAL_SRC_FILES +=ManhattanDistance.h
                         LOCAL_SRC_FILES +=MemoryResidentMatrix.h
                          LOCAL_SRC_FILES +=PAA.h
                           LOCAL_SRC_FILES +=PartialWindowMatrix.h
                            LOCAL_SRC_FILES +=SearchWindow.cpp
                            LOCAL_SRC_FILES +=SearchWindow.h
                            LOCAL_SRC_FILES +=TimeSeries.h
                           LOCAL_SRC_FILES +=TimeSeriesPoint.h
                           LOCAL_SRC_FILES +=TimeWarpInfo.h

    LOCAL_LDLIBS := -llog
    include $(BUILD_SHARED_LIBRARY)