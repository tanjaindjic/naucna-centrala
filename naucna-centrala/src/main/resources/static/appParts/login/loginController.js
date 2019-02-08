(function () {
    "use strict";

    mainModule.controller('loginController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {

            $scope.loginFields = [];
            $scope.registrationField;
            var init = function () {

                if (mainService.getJwtToken()) {
                    mainService.goToState("core", true)
                }

                $http({
                    method: 'GET',
                    url: ROOT_PATH + "korisnik/login"
                }).then(function successCallback(response) {
                    console.log(JSON.stringify(response.data))
                    var obj = response.data;
                    $scope.formFields = obj["formField"];
                    console.log(obj["taskId"])
                    console.log(obj["processInstanceId"])
                    console.log($scope.formFields)
                    window.localStorage.setItem('taskId', obj["taskId"]);
                    window.localStorage.setItem('processInstanceId', obj["processInstanceId"]);
                    $scope.loginFields = $scope.formFields;
                    $scope.registrationField = $scope.loginFields.pop();
                    console.log(JSON.stringify( $scope.registrationField))
                }, function errorCallback(response) {
                    console.log("grerska " + JSON.stringify(response))

                });

            }
            init();

            $scope.login = function () {
                var formData = mainService.getFormData($scope.loginFields);
                var payload = {
                    "taskId" : window.localStorage.getItem('taskId'),
                    "processInstanceId" : window.localStorage.getItem('processInstanceId'),
                    "formFields" : formData
                }
                console.log(payload)
                $http({
                    method: 'POST',
                    url: ROOT_PATH + "korisnik/login",
                    data: JSON.stringify(payload)
                }).then(function successCallback(response) {
                    console.log(response.data.token)
                    mainService.setJwtToken(response.data.token);
                    console.log("Postavljen: " + window.localStorage.getItem('token'))
                    mainService.goToState("core.home", true)
                }, function errorCallback(response) {
                    console.log("grerska " + JSON.stringify(response.data))

                });

            }

            $scope.registracija = function(){
                var formData = mainService.getFormData($scope.loginFields);
                var regField = {
                    "fieldId": $scope.registrationField.id,
                    "fieldValue": true
                }
                formData.push(regField);
                var payload = {
                    "taskId" : window.localStorage.getItem('taskId'),
                    "processInstanceId" : window.localStorage.getItem('processInstanceId'),
                    "formFields" : formData
                }
                $http({
                    method: 'POST',
                    url: ROOT_PATH + "korisnik/noAccount",
                    data: JSON.stringify(payload)
                }).then(function successCallback(response) {
                    mainService.goToState("core.register", true);
                }, function errorCallback(response) {
                    console.log("grerska " + JSON.stringify(response.data))

                });


            }
        }
    ]);
})();