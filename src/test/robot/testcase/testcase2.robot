*** Settings ***
Documentation     Robot Framework Example
Resource    ../resources/resource1.robot

*** Test Cases ***
javaee7-angular
    Open Remote Chrome through Selenium Hub    ${APP_URL}    ${GRID_URL}
    Input Text       name    Naam
    Input Text       description    Beschrijving
    Click Button     xpath=//button[@class='btn btn-primary' and @type='button']
    Click Link       xpath=//a[text()='2']
    Click Element    xpath=//span[text()='Kurama']
    ${imageURL}=     Get Element Attribute    //div[@ng-if='person.imageUrl']/img[1]@src
    Should Be Equal As Strings     http://os-fase2-jeeapp-demojavateam-o.cloudapps.belastingdienst.nl/pic/Kurama.png    ${imageURL}
    &{Dummy}         Create Dictionary    key1=value1    key2=value2
    Collections.Set To Dictionary    ${Dummy}    key3=value3
    Close Browser
    [Teardown]    Close All Browsers
