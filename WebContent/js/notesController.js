angular.module('app').controller('notesController', function($http, $scope, $modal) {
	$scope.backupData = undefined;
	$scope.messages = [];

	$scope.init = function() {
		$http.get("rest/getMessages").success(function(data) {
			$scope.messages = data;
		}).error(function(msg, code) {
			alert(msg);
		});
	};

	$scope.init();

	$scope.newMessage = function() {
		$http.get("rest/addMessage", {
			params : {
				"message" : $scope.message,
			}
		}).success(function(data) {
			$scope.messages.push(data);
		}).error(function(error, status) {
			alert(error);
		});
	};

	$scope.editMessage = function(message) {
		$scope.backup = message.text;
		message.edit = true;
	};

	$scope.cancel = function(message) {
		message.text = $scope.backup;
		message.edit = false;
	};

	$scope.saveMessage = function(message) {
		$http.get("rest/editMessage", {
			params : {
				"message" : message.text,
				"messageId" : message.messageId
			}
		}).error(function(error, status) {
			message.text = $scope.backup;
			alert(error);
		});
		message.edit = false;
	};

	$scope.deleteMessage = function(message, position) {
		if (!confirm("Na pewno chcesz usunąć tą wiadomość?")) {
			return;
		}
		$http.get("rest/deleteMessage", {
			params : {
				"messageId" : message.messageId
			}
		}).success(function(data) {
			$scope.messages.splice(position, 1);
		}).error(function(error, status) {
			alert(error);
		});
	};

	$scope.isEditor = function(note) {
		var result = note.editors.filter(function(e) {
			return e.login === $scope.$parent.loggedUser.login;
		});
		return note.owner.login === $scope.$parent.loggedUser.login || result.length > 0;
	};

	$scope.permissions = function(note) {
		var modalInstance = $modal.open({
			templateUrl : 'permissions.html',
			controller : 'permissionsController',
			size : 'lg',
			resolve : {
				items : function() {
					return [ note, $scope.$parent.loggedUser ];
				}
			}
		});
	};
});