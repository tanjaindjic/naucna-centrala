(function() { "use strict";

    mainModule.controller('homeController', [ '$scope', '$http', '$window','$localStorage','$location', '$stateParams','$state','mainService',
        function($scope,  $http, $window, $localStorage, $location,  $stateParams, $state, mainService) {

            var init = function () {
                console.log("init home")
                $scope.sub = mainService.getSub();
                $scope.placeniCasopisi = [];

                if( $scope.sub!=""){
                    $http({
                        method: 'GET',
                        url: ROOT_PATH + "korisnik/" + $scope.sub+"/placeniCasopisi",
                        headers : mainService.createAuthorizationTokenHeader()
                    }).then(function(result){
                        console.log(result)
                        $scope.placeniCasopisi = result;
                    });
                }

                var myDataPromise = mainService.getCasopisi();
                    myDataPromise.then(function(result) {
                         $scope.casopisi = result;
                         console.log($scope.casopisi);
                });


            }
            init();

            $scope.kupi = function(id){
               $stateParams.id = id;
               $location.path("/casopis/"+id)
               //$state.go( "core.casopis", { id: $stateParams.id} );
            }

            $scope.nijeKupljen = function(openAccess, id){
               /* if($scope.placeniCasopisi.indexOf(id.toString())==-1)
                    if(!openAccess)
                        return true;
                    else return false;
                else*/ return !openAccess;

            }

        }
    ]);
})();