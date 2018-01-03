*** Settings ***
Documentation     Robot Framework Example
Library           Collections
Library           Selenium2Library

*** Variables ***
${GRID_URL}       http://selenium-hub-ontwikkelteam-demo.cloudapps.ont.belastingdienst.nl/wd/hub
${APP_URL}        http://os-fase2-jeeapp-ontwikkelteam-demo.cloudapps.ont.belastingdienst.nl
${BROWSER}        chrome
${ALIAS}          None

*** Keywords ***
Open Remote Chrome through Selenium Hub
    [Documentation]         Start Chrome browser on Selenium Grid
    [Arguments]             ${url}    ${selenium hub}
    ${desired capabilities}=    Evaluate    {'browserName': 'chrome'}
    ${executor}    Evaluate    sys.modules['selenium.webdriver'].remote.remote_connection.RemoteConnection(${selenium hub}, resolve_ip=False)    sys, selenium.webdriver
    Create Webdriver    Remote    command_executor=${executor}    desired_capabilities=${desired capabilities}
    Go To     ${url}

*** Test Cases ***
javaee7-angular
    Open Remote Chrome through Selenium Hub    ${APP_URL}    ${GRID_URL}
    Maximize Browser Window
    Input Text       name    Naam
    Input Text       description    Beschrijving
    Click Button     xpath=//button[@class='btn btn-primary' and @type='button']
    Click Link       xpath=//a[text()='2']
    Click Element    xpath=//span[text()='Kurama']
    ${imageURL}=     Selenium2Library.Get Element Attribute    //div[@ng-if='person.imageUrl']/img[1]@src
    Should Be Equal As Strings    http://img1.wikia.nocookie.net/__cb20140818171718/naruto/images/thumb/7/7b/Kurama2.png/300px-Kurama2.png    ${imageURL}
    Collections.Set To Dictionary    ${Dummy}    key1=value1    key2=value2
    Close Browser
