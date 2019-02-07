(function() { "use strict";

    mainModule.controller('casopisController', [ '$scope', '$http', '$location', '$window','$localStorage', '$stateParams','$state','mainService',
        function($scope,  $http, $location, $window, $localStorage, $stateParams, $state, mainService) {
            $scope.cena = 0;
            $scope.casopis = [];
            $scope.radovi = [];
            $scope.pretplata = false;
            var init = function () {

                console.log("init casopis")
                var id = $stateParams.id;
                $scope.id = /[^/]*$/.exec(window.location.href)[0];
                console.log($scope.id)
                var myDataPromise = mainService.getCasopis($scope.id);
                    myDataPromise.then(function(result) {
                         $scope.casopis = result;
                         console.log($scope.casopis);
                         $scope.cena = $scope.casopis.cena;
                         //namestiCenu();
                });

                $http({
                    method: 'GET',
                    url: ROOT_PATH + "casopis/" + id + "/radovi",
                    headers : mainService.createAuthorizationTokenHeader()
                }).then(function(result){
                    $scope.radovi = result.data;
                    console.log($scope.radovi.length)
                });

            }
            init();

            var namestiCenu = function(){
                $scope.cena = $scope.casopis.cena;
                 $scope.$apply()
            }

            $('input[type=radio][name=opcijeKupovine]').change(function() {

                if (this.value == 'primerak') {
                    $scope.cena = $scope.casopis.cena;
                    $scope.pretplata = false;
                }
                else if (this.value == 'pretplata1g') {
                    $scope.cena = $scope.casopis.cena*12;
                    $scope.pretplata = true;
                }

                 console.log($scope.cena)
                 $scope.$apply()
            });

            $scope.kupi = function(){
                if( mainService.getSub()==""){
                     mainService.goToState("login", true);
                     return;
                }
                var payload = {
                    "casopisId": $scope.id,
                    "radId": null,
                    "username" : mainService.getSub(),
                    "pretplata": $scope.pretplata,
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
                            console.log(response.data.poruka);
                        }
                    }, function errorCallback(response) {
                       console.log("fail")

                    });
            }

           $scope.naRad = function(id){
            console.log(id)
              $location.path("/rad/"+id)
          }


        }
    ]);
})();