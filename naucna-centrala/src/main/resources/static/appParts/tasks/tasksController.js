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
            document.getElementById("prikaziMojeRecenzije").style.display="none";
            document.getElementById("prikaziRecenzirane").style.display="none";
       }
       $scope.recenzije = function(){
            document.getElementById("prikaziNoveRadove").style.display="none";
            document.getElementById("prikaziMojeRecenzije").style.display="block";
            document.getElementById("prikaziRecenzirane").style.display="none";
       }
       $scope.recenziraniRadovi = function(){
            document.getElementById("prikaziNoveRadove").style.display="none";
            document.getElementById("prikaziMojeRecenzije").style.display="none";
            document.getElementById("prikaziRecenzirane").style.display="block";
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
            $location.path("/dodajRecenzenta/"+id)
       }

       $scope.obrisi = function(id){
           $http({
               method: 'DELETE',
               url: ROOT_PATH + "rad/" + id,
               headers : mainService.createAuthorizationTokenHeader()
           }).then(function(result){
               alert(result.data)
           });
       }

       $scope.objavi = function(id){
           $http({
                  method:"GET",
                  url:ROOT_PATH + "rad/" + id + "/index",
                  headers : mainService.createAuthorizationTokenHeader()
           }).then(function(result){
                  alert(result.data)

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

       });
        }




   }
]);