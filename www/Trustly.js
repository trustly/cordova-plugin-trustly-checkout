'use strict';

var exec = require('cordova/exec');

var TrustlyPlugin = (function () {

	function TrustlyPlugin () {}

	TrustlyPlugin.prototype = {

		showTrustlyCheckout: function (arg) {
			return new Promise(function (resolve, reject) {
				exec(resolve, reject, 'Trustly', 'showTrustlyCheckout', [arg]);
			});
		}

	};
	return TrustlyPlugin;
})();

var Trustly = new TrustlyPlugin();

module.exports = Trustly;
