# Cordova Plugin for Trustly Checkout

This plugin helps you integrate the Trustly Checkout into your Cordova project.

## Integration

Add the plugin to your project

```shell
cordova plugin add https://github.com/trustly/cordova-plugin-trustly-checkout
```

## Usage

Pass your Checkout URL to the `showTrustlyCheckout` function.

```javascript
trustly
  .showTrustlyCheckout("<YOUR-TRUSTLY-CHECKOUT-URL>")
  .then((successUrl) => {
    //Checkout was successful
    //The Checkout view/activity will close automatically after this.
  })
  .catch((err) => {
    //Called on Checkout error and abort, check err.type to see which.
    //The Checkout view/activity will close automatically after this.
  });
```
