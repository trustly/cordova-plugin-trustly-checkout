@objc(TrustlyPlugin) class TrustlyPlugin : CDVPlugin {

    var callBackId: String?
	@objc(showTrustlyCheckout:)
	func showTrustlyCheckout(command: CDVInvokedUrlCommand) {
       if let checkoutURL = (command.arguments[0] as? String) {
        callBackId = command.callbackId
        let mainView = TrustlyWKWebView(checkoutUrl: checkoutURL, frame: self.viewController.view.bounds);
        mainView?.delegate = self
        let checkoutViewController = UIViewController()
        checkoutViewController.view = mainView;
        checkoutViewController.modalPresentationStyle = .fullScreen
        self.viewController.present(checkoutViewController, animated: true, completion: nil)

        } else {
            //No checkout url was specified
            let callbackId: String = command.callbackId
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: ["code": "error", "message": "input params not valid"])
            self.commandDelegate.send(result, callbackId: callbackId)
            self.viewController.dismiss(animated: true, completion: nil)
        }
	}
}

extension TrustlyPlugin: TrustlyCheckoutDelegate {
    func onTrustlyCheckoutRequstToOpenURLScheme(urlScheme: String) {
        if let url = URL(string: urlScheme) {
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
            } else {
                UIApplication.shared.openURL(url)
            }
        }
    }
    
    func onTrustlyCheckoutSuccessfull(urlString: String?) {
        let url = urlString ?? ""
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: url)
        self.commandDelegate.send(result, callbackId: callBackId)
        self.viewController.dismiss(animated: true, completion: nil)
    }
    
    func onTrustlyCheckoutAbort(urlString: String?) {
        let url = urlString ?? ""
        let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: ["code": "error", "message": "Checkout aborted", "type": "abort", "url": url])
        self.commandDelegate.send(result, callbackId: callBackId)
        self.viewController.dismiss(animated: true, completion: nil)
    }
    
    func onTrustlyCheckoutError() {
        let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: ["code": "error", "message": "Checkout error", "type": "error"])
        self.commandDelegate.send(result, callbackId: callBackId)
        self.viewController.dismiss(animated: true, completion: nil)
    }
    
    
}



