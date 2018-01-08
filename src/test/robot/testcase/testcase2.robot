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
    Should Be Equal As Strings    http://img1.wikia.nocookie.net/__cb20140818171718/naruto/images/thumb/7/7b/Kurama2.png/300px-Kurama2.png    ${imageURL}
    &{Dummy}         Create Dictionary    key1=value1    key2=value2
    Collections.Set To Dictionary    ${Dummy}    key3=value3
    Close Browser
    [Teardown]    Close All Browsers
