(function() { "use strict";

    mainModule.controller('homeController', [ '$scope', '$http', '$window','$localStorage','$location', '$stateParams','$state','mainService',
        function($scope,  $http, $window, $localStorage, $location,  $stateParams, $state, mainService) {

            var init = function () {
                console.log("init home")
                $scope.sub = mainService.getSub();
                console.log($scope.sub)

                var myDataPromise = mainService.getCasopisi();
                    myDataPromise.then(function(result) {
                         $scope.casopisi = result;
                         console.log($scope.casopisi);
                });
            }
            init();

            $scope.kupi = function(id){
               console.log(id)
               $stateParams.id = id;
               $state.go( "core.casopis", { id: $stateParams.id} );
            }

        }
    ]);
})();