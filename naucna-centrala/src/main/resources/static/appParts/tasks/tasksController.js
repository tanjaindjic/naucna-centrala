mainModule.controller('tasksController', ['$http','$scope', '$window', 'mainService','PDFViewerService','$location',
       function($http, $scope, $window, mainService, pdf, $location){


       var init = function(){

           if( mainService.getSub()==""){
                mainService.goToState("login", true);
                return;
           }

           $http({
               method: 'GET',
               url: ROOT_PATH + "urednik/" + mainService.getSub() + "/noviRadovi",
               headers : mainService.createAuthorizationTokenHeader()
           }).then(function(result){
               console.log(result)
               $scope.noviRadovi = result.data;
           });
       }
       init();

       $scope.radovi = function(){
            document.getElementById("prikaziNoveRadove").style.display="block";
            document.getElementById("prikaziNaDoradi").style.display="none";
            document.getElementById("prikaziNaCekanju").style.display="none";
            document.getElementById("prikaziRecenzije").style.display="none";
       }
       $scope.dorade = function(){
            $http({
               method: 'GET',
               url: ROOT_PATH + "urednik/" + mainService.getSub() + "/dorade",
               headers : mainService.createAuthorizationTokenHeader()
           }).then(function(result){
               console.log(result)
               $scope.naDoradi = result.data;
           });
            document.getElementById("prikaziNoveRadove").style.display="none";
            document.getElementById("prikaziNaDoradi").style.display="block";
            document.getElementById("prikaziNaCekanju").style.display="none";
            document.getElementById("prikaziRecenzije").style.display="none";
       }

        $scope.cekanje = function(){
                   $http({
                      method: 'GET',
                      url: ROOT_PATH + "urednik/" + mainService.getSub() + "/naCekanju",
                      headers : mainService.createAuthorizationTokenHeader()
                  }).then(function(result){
                      console.log(result)
                      $scope.naCekanju = result.data;
                  });
                   document.getElementById("prikaziNoveRadove").style.display="none";
                   document.getElementById("prikaziNaDoradi").style.display="none";
                   document.getElementById("prikaziNaCekanju").style.display="block";
                   document.getElementById("prikaziRecenzije").style.display="none";
              }
        $scope.recenzije = function(){
                $http({
                          method:"GET",
                          url:ROOT_PATH + "urednik/" + mainService.getSub() + "/zaReviziju",
                          headers : mainService.createAuthorizationTokenHeader()
                   }).then(function(result){
                        console.log(result)
                        $scope.sveRecenzije = result.data;
                   });
                document.getElementById("prikaziNoveRadove").style.display="none";
               document.getElementById("prikaziNaDoradi").style.display="none";
               document.getElementById("prikaziNaCekanju").style.display="none";
               document.getElementById("prikaziRecenzije").style.display="block";
        }


       $scope.pogledajRad = function(id){

            $http({method: 'GET', url: ROOT_PATH + "rad/download/" + id, headers : mainService.createAuthorizationTokenHeader()})
              .then(function(result){
                console.log(result)
                 var anchor = angular.element('<a/>');
                 anchor.attr({
                     href: 'data:attachment/pdf;charset=utf-8,' + encodeURI(result.data),
                     target: '_blank',
                     download: 'rad.pdf'
                 })[0].click();

              }, function errorCallback(response) {
                   console.log("grerska " + JSON.stringify(response))

               });

       }

       $scope.dodajRecenzenta = function(id){
            $http({
              method: 'GET',
              url: ROOT_PATH + "rad/" + id + "/naRecenziranje",
              headers : mainService.createAuthorizationTokenHeader()
            }).then(function(result){
                alert(result.data)
                location.reload();
            });
       }

        $scope.odabirRecenzenta = function(id){
            $location.path("/dodajRecenzenta/"+id)
        }

       $scope.obrisi = function(id){
           $http({
               method: 'DELETE',
               url: ROOT_PATH + "rad/" + id,
               headers : mainService.createAuthorizationTokenHeader()
           }).then(function(result){
               alert(result.data)
               location.reload();
           });
       }

       $scope.objavi = function(id){
           $http({
                  method:"GET",
                  url:ROOT_PATH + "rad/" + id + "/index",
                  headers : mainService.createAuthorizationTokenHeader()
           }).then(function(result){
                  alert(result.data)
                  location.reload();

           });
       }

        $scope.dorada = function(id){
            $scope.odabraniRad = id;
            $('#modalKomentar').modal('show');
        }

        $scope.posalji = function(){
        $('#modalKomentar').modal('hide');
        $http({
              method:"POST",
              url:ROOT_PATH + "rad/" + $scope.odabraniRad + "/dorada",
              data: JSON.stringify($('#komentar').val()),
              headers : mainService.createAuthorizationTokenHeader()
       }).then(function(result){
              alert(result.data)
              location.reload();

       });
        }




   }
]);