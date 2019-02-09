(function() { "use strict";

    mainModule.controller('dodajRecenzentaController', [ '$scope', '$http', '$location', '$window','$localStorage', '$stateParams','$state','mainService',
        function($scope,  $http, $location, $window, $localStorage, $stateParams, $state, mainService) {
            $scope.udaljenost = 0;
            $scope.recenzenti = [];
            $scope.rad = {};
            var init = function () {

                return $http({
                    method:"GET",
                    url:ROOT_PATH + "rad/" + id + "/recenzenti",
                    headers : mainService.createAuthorizationTokenHeader()
                }).then(function(result){
                    $scope.recenzenti = result;
                    console.log($scope.recenzenti)
                });

                console.log("init rad")
                var id = $stateParams.id;
                $scope.id = /[^/]*$/.exec(window.location.href)[0];
                console.log($scope.id)
                var myDataPromise = mainService.getRad($scope.id);
                    myDataPromise.then(function(result) {
                         $scope.rad = result;
                         console.log($scope.rad);


                });



            }
            init();



        }
    ]);
})();