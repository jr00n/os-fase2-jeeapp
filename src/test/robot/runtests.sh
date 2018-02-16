pybot --outputdir ./output --timestamp --loglevel DEBUG ./testcase/testcase1.robot || ERROR=true
pybot --outputdir ./output --timestamp --loglevel DEBUG ./testcase/testcase2.robot || ERROR=true

#Fail the build if there was an error
if [ $ERROR ]
then
    exit -1
fi
                    
