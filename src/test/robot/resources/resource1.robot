*** Settings ***
Documentation     Resources needed for testcase
Library           Collections
Library           ExtendedSelenium2Library    implicit_wait=5

*** Variables ***
${GRID_URL}
${APP_URL}
${BROWSER}        chrome
${ALIAS}          None

*** Keywords ***
Open Remote Chrome through Selenium Hub
    [Documentation]         Start Chrome browser on Selenium Grid
    [Arguments]             ${url}    ${selenium hub}
    ${desired capabilities}=    Evaluate    {'browserName': 'chrome'}
    ${executor}    Evaluate    sys.modules['selenium.webdriver'].remote.remote_connection.RemoteConnection('${selenium hub}', resolve_ip=False)    sys, selenium.webdriver
    Create Webdriver    Remote    command_executor=${executor}    desired_capabilities=${desired capabilities}
    # Maximize Browser Window # werkt niet met headless framebuffer XVFB
    # Selenium Browser nodes zijn geconfigureerd met (virtual) screen size 1360x1020x24bit
    Set Window Size    1360    1020
    Go To     ${url}
