angular.module('app').controller('registerController',
		function($http, $scope, $modal, $modalInstance) {

			$scope.register = function() {
				$http.get("rest/register", {
					params : {
						"name" : $scope.name,
						"login" : $scope.login,
						"password" : $scope.password
					}
				}).success(function(data) {
					$modalInstance.close();

				}).error(function(error, status) {
					alert(error);
				});
			}
		});