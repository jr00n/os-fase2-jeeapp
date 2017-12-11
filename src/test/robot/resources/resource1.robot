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

Start_Chrome
    [Arguments]    ${URL}
    [Documentation]    Bij het starten van Chrome vanaf versie 57 verschijnt een pop-up-foutmelding over een extension die niet geladen kan worden.
    ...
    ...    Om het uitzetten van de blacklist te laten werken moet RobotFramework wel als administrator gestart worden.
    ${options}=    Evaluate    sys.modules['selenium.webdriver'].ChromeOptions()    sys, selenium.webdriver
    Call Method    ${options}    add_argument    disable-gpu
    Create WebDriver    Chrome    chrome_options=${options}
    Go To    ${URL}
    Maximize Browser Window

Teardown_browser
    Close All Browsers
