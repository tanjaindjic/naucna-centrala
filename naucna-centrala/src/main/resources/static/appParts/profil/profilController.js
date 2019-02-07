mainModule.controller('profilController', ['$http','$scope', '$window', 'mainService',
       function($http, $scope, $window, mainService){

       var init = function(){

           $scope.prikaziRadove = true;
           $scope.prikaziCasopise = false;
           $scope.prikaziPretplate = false;

           if( mainService.getSub()==""){
                mainService.goToState("login", true);
                return;
           }

           $http({
               method: 'GET',
               url: ROOT_PATH + "korisnik/" + mainService.getSub(),
               headers : mainService.createAuthorizationTokenHeader()
           }).then(function(result){
               console.log(result)
               $scope.korisnik = result.data;
           });
       }
       init();

       $scope.radovi = function(){
            document.getElementById("prikaziRadove").style.display="block";
            document.getElementById("prikaziCasopise").style.display="none";
            document.getElementById("prikaziPretplate").style.display="none";
       }
       $scope.casopisi = function(){
            document.getElementById("prikaziRadove").style.display="none";
            document.getElementById("prikaziCasopise").style.display="block";
            document.getElementById("prikaziPretplate").style.display="none";
       }
       $scope.pretplate = function(){
            document.getElementById("prikaziRadove").style.display="none";
            document.getElementById("prikaziCasopise").style.display="none";
            document.getElementById("prikaziPretplate").style.display="block";
       }

       $scope.skiniRad = function(id){
            $http({
              method: 'GET',
              url: ROOT_PATH + "rad/download/" + id,
              headers : mainService.createAuthorizationTokenHeader()
            }).then(function(result){
              return result.data;
            });
       }

       $scope.skiniCasopis = function(id){
           $http({
             method: 'GET',
             url: ROOT_PATH + "casopis/download/" + id,
             headers : mainService.createAuthorizationTokenHeader()
           }).then(function(result){
             console.log(result)
           });
       }






   }
]);