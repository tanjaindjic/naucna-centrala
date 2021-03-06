(function () {
    "use strict";

    mainModule.controller('registerController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {


            $scope.init = function () {
               
                if (mainService.getJwtToken()) {
                    mainService.goToState("core.home", true)
                }

                 $http({
                    method: 'GET',
                    url: ROOT_PATH + "korisnik/register"
                }).then(function successCallback(response) {
                    console.log(JSON.stringify(response.data))
                    var obj = response.data;
                    $scope.formFields = obj["formField"];
                    console.log(obj["taskId"])
                    console.log(obj["processInstanceId"])
                    console.log($scope.formFields)
                    window.localStorage.setItem('taskId', obj["taskId"]);
                    window.localStorage.setItem('processInstanceId', obj["processInstanceId"]);
                }, function errorCallback(response) {
                    console.log("grerska " + JSON.stringify(response))

                });

            }

            $scope.register = function () {

                var formData = mainService.getFormData($scope.formFields);
                var payload = {
                    "taskId" : window.localStorage.getItem('taskId'),
                    "processInstanceId" : window.localStorage.getItem('processInstanceId'),
                    "formFields" : formData
                }
                console.log(JSON.stringify(payload))
                $http({
                    method: 'POST',
                    url: ROOT_PATH + "korisnik/register",
                    data: JSON.stringify(payload)
                }).then(function successCallback(response) {
                    alert("Uspešna registracija.")
                    mainService.goToState("core.home", true)
                }, function errorCallback(response) {
                    alert("Nespešna registracija.")

                });

            }
        }
    ]);
})();