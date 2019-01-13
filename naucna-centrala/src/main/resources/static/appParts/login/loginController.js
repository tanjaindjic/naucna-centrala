(function () {
    "use strict";

    mainModule.controller('loginController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {


            $scope.init = function () {
                $scope.username = "";
                $scope.pass = "";
                if (mainService.getJwtToken()) {
                    mainService.goToState("core.home", true)
                }
            }


            $scope.login = function () {

                var payload = {
                    "username": $scope.username,
                    "password": $scope.pass
                }
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
        }
    ]);
})();