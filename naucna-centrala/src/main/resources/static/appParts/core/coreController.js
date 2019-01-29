(function () {
    'use strict';
    mainModule.controller('coreController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {


            var init = function () {
                 console.log("init core")
                if (mainService.getJwtToken()) {
                    document.getElementById("prijava").style.display = "none";
                    document.getElementById("odjava").style.display = "block";
                } else {
                    document.getElementById("prijava").style.display = "block";
                    document.getElementById("odjava").style.display = "none";
                }
                mainService.goToState("core.home", false);
            }
            init();

            $scope.odjava = function () {
                mainService.removeJwtToken();
                mainService.goToState("core.home", true);
            }

            $scope.prijava = function () {
                mainService.goToState("login", true);
            }

            $scope.novirad = function(){
 
                $http({
                    method: 'POST',
                    url: ROOT_PATH + "rad/prijavaRada",
                    headers : mainService.createAuthorizationTokenHeader()
                }).then(function successCallback(response) {
                    mainService.goToState("prijavarada", true);
                });
            }


        }
    ]);
})();