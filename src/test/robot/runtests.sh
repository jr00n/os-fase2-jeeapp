#!/bin/sh
if [ $# -lt 1 ]
then
  echo "Gebruik: $0 GRID_URL APP_URL"
  exit -1
fi


pybot --outputdir ./output --timestamp --loglevel DEBUG --variable GRID_URL:$1 --variable APP_URL:$2 ./testcase/testcase1.robot || ERROR=true
pybot --outputdir ./output --timestamp --loglevel DEBUG --variable GRID_URL:$1 --variable APP_URL:$2 ./testcase/testcase2.robot || ERROR=true

#Fail the build if there was an error
if [ $ERROR ]
then
    exit -1
fi
                    
