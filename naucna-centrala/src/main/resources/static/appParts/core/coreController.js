(function () {
    'use strict';
    mainModule.controller('coreController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {


            var init = function () {
                if (!mainService.getJwtToken()) {
                    mainService.goToState("login")
                } else $scope.sub = mainService.getSub();

            }
            init();

            $scope.odjava = function () {
                mainService.removeJwtToken();
                mainService.goToState("core.home", true);
            }

            $scope.novirad = function(){
 
                $http({
                    method: 'POST',
                    url: ROOT_PATH + "rad",
                    headers : createAuthorizationTokenHeader()
                }).then(function successCallback(response) {
                    mainService.goToState("core.prijavarada", true);
                });
            }


        }
    ]);
})();