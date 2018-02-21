#!/bin/sh
if [ $# -lt 1 ]
then
  echo "Gebruik: $0 GRID_URL APP_URL"
  exit -1
fi


pybot --outputdir ./output --timestamp --loglevel DEBUG ./testcase/testcase1.robot --variable GRID_URL:$1 --variable APP_URL:$2 || ERROR=true
pybot --outputdir ./output --timestamp --loglevel DEBUG ./testcase/testcase2.robot --variable GRID_URL:$1 --variable APP_URL:$2 || ERROR=true

#Fail the build if there was an error
if [ $ERROR ]
then
    exit -1
fi
                    
