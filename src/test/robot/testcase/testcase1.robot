*** Settings ***
Documentation     Resources needed for testcase
Library           Collections    # Standard
Library           Selenium2Library    implicit_wait=5    #GUI Web testing


*** Variables ***
${REMOTE_URL}     http://selenium-hub-ywb-test.cloudapps.ont.belastingdienst.nl:80/wd/hub
${BROWSER}        chrome
${ALIAS}          None

*** Keywords ***
Start Browser
    [Documentation]         Start browser on Selenium Grid
    [Arguments]              ${URL}
    Open Browser            ${URL}  ${BROWSER}  ${ALIAS}  ${REMOTE_URL}
    Maximize Browser Window

*** Test Cases ***
javaee7-angular
    [Setup]    Start Browser   http://os-fase2-jeeapp-ywb-test.cloudapps.ont.belastingdienst.nl/
    Input Text    name    Naam
    Input Text    description    Beschrijving
    Click Button    xpath=//button[@class='btn btn-primary' and @type='button']
    Click Link    xpath=//a[text()='2']
    Click Element    xpath=//span[text()='Kurama']
    ${imageURL}=    Selenium2Library.Get Element Attribute    //div[@ng-if='person.imageUrl']/img[1]@src
    Should Be Equal As Strings    http://img1.wikia.nocookie.net/__cb20140818171718/naruto/images/thumb/7/7b/Kurama2.png/300px-Kurama2.png    ${imageURL}
    Collections.Set To Dictionary    ${Dummy}    key1=value1    key2=value2
    [Teardown]    Teardown_browser
