(function () {
    "use strict";

    mainModule.controller('registerController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {


            $scope.init = function () {
                $scope.username = "";
                $scope.pass = "";
                $scope.email = "";
                if (mainService.getJwtToken()) {
                    mainService.goToState("core.home", true)
                }

                 $http({
                    method: 'GET',
                    url: ROOT_PATH + "korisnik/register"
                }).then(function successCallback(response) {
                    console.log(response.data)
                    window.localStorage.setItem('taskId', response.data.taskId);
                    console.log("Postavljen: " + window.localStorage.getItem('taskid'))
                }, function errorCallback(response) {
                    console.log("grerska " + JSON.stringify(response))

                });

            }


            $scope.register = function () {

                var payload = {
                    "username": $scope.username,
                    "password": $scope.pass,
                    "email": $scope.email,
                    "taskId" : window.localStorage.getItem('taskId')
                }
                $http({
                    method: 'POST',
                    url: ROOT_PATH + "korisnik/register",
                    data: JSON.stringify(payload)
                }).then(function successCallback(response) {
                    alert("Succ")
                    mainService.goToState("core.home", true)
                }, function errorCallback(response) {
                    alert("fail :(")

                });

            }
        }
    ]);
})();