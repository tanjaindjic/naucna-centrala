(function() { "use strict";

    mainModule.controller('radController', [ '$scope', '$http', '$location', '$window','$localStorage', '$stateParams','$state','mainService',
        function($scope,  $http, $location, $window, $localStorage, $stateParams, $state, mainService) {
            $scope.cena = 0;
            $scope.casopis = [];
            $scope.rad = {};
            var init = function () {

                console.log("init rad")
                var id = $stateParams.id;
                $scope.id = /[^/]*$/.exec(window.location.href)[0];
                console.log($scope.id)
                var myDataPromise = mainService.getRad($scope.id);
                    myDataPromise.then(function(result) {
                         $scope.rad = result;
                         $scope.casopis = $scope.rad.casopis;
                         console.log($scope.rad);
                          $scope.cena = $scope.rad.cena;
                         //namestiCenu();
                });



            }
            init();

            var namestiCenu = function(){
                if(!$scope.casopis.openAccess)
                    $scope.cena = $scope.rad.cena;
                else $scope.cena = 0;
                 $scope.$apply()
            }

            $scope.kupi = function(){
                if( mainService.getSub()==""){
                     mainService.goToState("login", true);
                }
                var payload = {
                    "casopisId": $scope.casopis.id,
                    "radId": $scope.id,
                    "username" : mainService.getSub(),
                    "pretplata": false,
                    "cena": $scope.cena
                }
                $http({
                        method: 'POST',
                        url: ROOT_PATH + "korisnik/kupi",
                        data: JSON.stringify(payload)
                    }).then(function successCallback(response) {
                        console.log(response.headers('Location'))
                         if(response.headers('Location')){
                            $window.location.href = response.headers('Location');
                        }else{
                            alert(response.data.poruka);
                        }
                    }, function errorCallback(response) {
                       alert("fail")

                    });
            }

             $scope.naRad = function(id){
               $stateParams.id = id;
               $location.path("/casopis/"+id)
               //$state.go( "core.casopis", { id: $stateParams.id} );
            }



        }
    ]);
})();