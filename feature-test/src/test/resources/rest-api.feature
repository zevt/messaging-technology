Feature: RestApi
  @RestApi
  Scenario: Test url response
    When A Request to Server
    Then Server return the request url