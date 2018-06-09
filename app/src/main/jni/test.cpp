//
// Created by Wang on 2018/4/10.
//
#include<test.h>
#include <iostream>
#include "DTW.h"
#include "FastDTW.h"
#include "EuclideanDistance.h"
#include"time.h"
using namespace std;

using namespace fastdtw;
double testFastDTW(double*sample1,int sampleLength1,double*sample2,int sampleLength2)
{
    TimeSeries<double,1> tsI;
    for (int i = 0; i<sampleLength1; ++i) {
        tsI.addLast(i, TimeSeriesPoint<double,1>(sample1+i));
    }
    TimeSeries<double,1> tsJ;
    for (int i = 0;i<sampleLength2; ++i)
    {
        tsJ.addLast(i, TimeSeriesPoint<double,1>(sample2+i));
    }

    TimeWarpInfo<double> info =  FAST::getWarpInfoBetween(tsI,tsJ,EuclideanDistance());
    return info.getDistance();
    //cout<<"Warp Distance by DTW:%lf\n"<<info.getDistance()<<endl;
   // info.getPath()->print(std::cout);
}
