(function() { "use strict";

    mainModule.controller('casopisController', [ '$scope', '$http', '$window','$localStorage','$location', '$stateParams','$state','mainService',
        function($scope,  $http, $location, $window, $localStorage, $stateParams, $state, mainService) {
            $scope.cena = 0;
            $scope.casopis = [];
            $scope.radovi = [];
            var init = function () {

                console.log("init casopis")
                var id = $stateParams.id;

                var myDataPromise = mainService.getCasopis(id);
                    myDataPromise.then(function(result) {
                         $scope.casopis = result;
                         console.log($scope.casopis);
                         namestiCenu();
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
                }
                else if (this.value == 'pretplata1g') {
                    $scope.cena = $scope.casopis.cena*12;
                }
                else if (this.value == 'pretplata2g') {
                    $scope.cena = $scope.casopis.cena*24;
                }
                 console.log($scope.cena)
                 $scope.$apply()
            });

            $scope.kupi = function(id){

            }


        }
    ]);
})();