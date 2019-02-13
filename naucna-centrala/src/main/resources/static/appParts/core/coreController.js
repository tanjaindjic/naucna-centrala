(function () {
    'use strict';
    mainModule.controller('coreController', ['$sce', '$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($sce, $scope, $http, $window, $localStorage, $location, $stateParams, mainService) {

            $scope.results = [];
            $scope.brPolja = 0;


            var init = function () {
                $scope.sub = mainService.getSub();
                 console.log("init core")
                if (mainService.getJwtToken()) {
                    document.getElementById("prijava").style.display = "none";
                    document.getElementById("odjava").style.display = "block";
                } else {
                    document.getElementById("prijava").style.display = "block";
                    document.getElementById("odjava").style.display = "none";
                }
            }
            init();

            $scope.odjava = function () {
                mainService.removeJwtToken();
                $http({
                    method: 'GET',
                    url: ROOT_PATH + "korisnik/logout"
                })
                mainService.goToState("core.home", true);
            }

            $scope.prijava = function () {
                mainService.goToState("core.login", true);
            }

            $scope.trazi = function(){
                var zaPretragu = document.getElementById("zaPretragu").value;
                console.log("uneo: " + zaPretragu)
                var myDataPromise = mainService.basicQuery(zaPretragu);
                    myDataPromise.then(function(result) {
                         console.log(result)
                         $scope.results = result;
                         console.log($scope.results[0])
                          $('#exampleModalLong').modal('show');

                });


            }

            $scope.profil = function(){
                if( mainService.getSub()==""){
                     mainService.goToState("core.login", true);
                     return;
                }
                mainService.goToState("core.profil", false);
            }

            $scope.taskovi = function(){
                if( mainService.getSub()==""){
                     mainService.goToState("core.login", true);
                     return;
                }
                mainService.goToState("core.tasks", false);
            }

            $scope.prijavljeniRadovi = function(){
                if( mainService.getSub()==""){
                     mainService.goToState("core.login", true);
                     return;
                }
                mainService.goToState("core.prijavljeniRadovi", false);
            }

            $('#naprednaPretragaModal').on('hidden.bs.modal', function () {
                $("#upit0").val("");
                $('#dodatnaPolja').html('');
                $scope.brPolja = 0;
            })


            $scope.dodajPolja = function(){
                $scope.brPolja+=1;
                var container = document.createElement('div');
                container.id = "polje" + $scope.brPolja;

                $("#dodatnaPolja").append(container);
                var html =
                "<div id=\"polje" + $scope.brPolja +"\" class=\"col-md-8 text\" style=\"display:inline;\">"+
                    "<div class=\"form-inline\" style=\"display:inline;\">"+
                        "<div class=\"form-group\" style=\"display:inline;\">"+
                            "<select style=\"width:80px; display:inline;\" id=\"operator" + $scope.brPolja + "\" class=\"form-control choice-select\" >"+
                                "<option value=\"i\">I</option>"+
                                "<option value=\"ili\">ILI</option>"+
                            "</select>"+
                        "</div>"+
                    "</div>"+
                     "<span>      </span>"+
                    "<div class=\"form-inline\" style=\"display:inline;\">"+
                        "<div class=\"form-group\" style=\"display:inline;\">"+
                            "<select id=\"zona"+ $scope.brPolja+"\" class=\"form-control choice-select\" style=\"display:inline;width:150px\">"+
                                "<option value=\"naslov\">Sva polja</option>"+
                                "<option value=\"naslov\">Naslov</option>"+
                                "<option value=\"sadrzaj\">Sadržaj</option>"+
                                "<option value=\"autor\">Autori</option>"+
                                "<option value=\"kljucniPojmovi\">Ključni pojmovi</option>"+
                            "</select>"+
                        "</div>"+
                      "</div>"+
                    "<span>      </span>"+
                    "<div class=\"form-inline\" style=\"display:inline;\">"+
                        "<div class=\"form-group\"style=\"display:inline;\">"+
                            "<input id=\"upit" + $scope.brPolja + "\" class=\"form-control\" type=\"text\" placeholder=\"Upit\">"+
                        "</div>"+
                        "<span>      </span>"+
                        "<div class=\"form-group\" style=\"display:inline;\">"+
                            "<input id=\"checkbox" + $scope.brPolja + "\" type=\"checkbox\" value=\"\">"+
                            "<label style=\"display:inline;\">Fraza</label>"+
                            "<span>   </span>"+
                            "<button id=\"dugme" + $scope.brPolja + "\" type=\"button\" class=\"btn btn-primary\" ng-click=\"obrisi(this.id)\" style=\"float: right;display:inline;\"> Obriši</button>"+
                        "</div>"+
                    "</div>"+
                "</div>"+
                "<br><div><p></p></div>";


                $("#" + container.id).append(html);
            }

            $scope.obrisi = function(button_id){

                    console.log(button_id);
            }

            $scope.naprednaPretraga = function(){

                $('#exampleModalLong').modal() ;
                var upitArray = [];
                var zona0 =$( "#zona0 option:selected" ).val();
                var upit0 = $('#upit0').val();
                var isFraza0 = $('#checkbox0').is(':checked');
                console.log($("#operator0").text())
                var upit0 = {
                    "operator": $("#operator0 option:selected").text(),
                    "zona": zona0,
                    "upit": upit0,
                    "isFraza" : isFraza0

                }
                upitArray.push(upit0);


                var dodatnaPolja = $("#dodatnaPolja > div").length;
                console.log(dodatnaPolja);
                var i;
                if(dodatnaPolja>0){
                    for(i=1; i<=dodatnaPolja; i++){
                        var upit = {
                            "operator": $( "#operator" + i + " option:selected" ).text(),
                            "zona":  $( "#zona" + i + " option:selected" ).val(),
                            "upit": $('#upit' + i).val(),
                            "isFraza": $('#checkbox' + i).is(':checked')
                        }
                        upitArray.push(upit);
                    }
                }
                console.log(upitArray);

                var oblasti = [];
                $(".naucneOblasti:checkbox:checked").each(function(){
                    oblasti.push($(this).val());
                });
                var payload = {
                    "upiti": upitArray,
                    "naucneOblasti": oblasti
                }
                console.log(payload)
                $http({
                        method: 'POST',
                        url: ROOT_PATH + "search/advancedQuery",
                        data: JSON.stringify(payload),
                        headers : mainService.createAuthorizationTokenHeader()
                    }).then(function successCallback(response) {
                        $scope.results = response.data;
                        $('#naprednaPretragaModal').modal('hide');
                        $('#exampleModalLong').modal('show');
                    }, function errorCallback(response) {
                        console.log("grerska " + JSON.stringify(response))

                    });
            }

             $scope.naCasopis = function(id){
               $('#exampleModalLong').modal('hide');
               $location.path("/casopis/"+id)
            }

            $scope.naRad = function(id){
               $('#exampleModalLong').modal('hide');
               $location.path("/rad/"+id)
            }
             $scope.recenzije = function(){
               mainService.goToState("core.recenzije", true);
            }

            $scope.novirad = function(){
                 if( mainService.getSub()==""){
                     mainService.goToState("core.login", true);
                     return;
                }
                $http({
                    method: 'GET',
                    url: ROOT_PATH + "rad/prijavaRada",
                    headers : mainService.createAuthorizationTokenHeader()
                }).then(function successCallback(response) {
                    console.log("Response:")
                    console.log(JSON.stringify(response));
                    var obj = response.data;
                    window.localStorage.setItem('taskId', obj["taskId"]);
                    window.localStorage.setItem('processInstanceId', obj["processInstanceId"]);
                    mainService.goToState("core.noviRad", true);
                }, function errorCallback(response) {
                     console.log("grerska " + JSON.stringify(response))

                 });
            }

            $scope.trustDangerousSnippet = function(tekst){
            return $sce.trustAsHtml(tekst);

            }
        }

    ]);
})();