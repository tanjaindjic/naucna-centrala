(function () {
    "use strict";

    mainModule.service('mainService', ['$http', '$window', '$localStorage', '$state',
        function ($http, $window, $localStorage, $state) {

            this.goToState = function (state) {
                $state.go(state, {"sub": this.getSub()}, { reload: true });
            }

            this.parseJwt = function (token) {
                var base64Url = token.split('.')[1];
                var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
                return JSON.parse(window.atob(base64));
            };

            this.getSub = function () {
                if (this.getJwtToken()) {
                    var token = this.parseJwt(this.getJwtToken());
                    console.log("token: " + token)
                    console.log("subject : " + token["sub"])
                    return token["sub"];
                }else return "";
            }

            this.getJwtToken = function () {
                return localStorage.getItem(JWT_TOKEN);
            }


            this.removeJwtToken = function () {
                localStorage.removeItem(JWT_TOKEN);
            }

            this.createAuthorizationTokenHeader = function () {
                var token = this.getJwtToken();
                if (token) {
                    return {
                        "Authorization": "Bearer " + token
                    };
                } else {
                    return {};
                }
            }


        }
    ]);
})()