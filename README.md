# The Electric Imp BlinkUp SDK for Android #

This repo contains the Electric Imp BlinkUp™ SDK for Android that it is made available to Electric Imp customers. As such it mirrors the downloadable files hosted by the [Dev Center](https://developer.electricimp.com/manufacturing/sdkdocs), but in a form that allows customer developers to add the SDK to their app project repos as a sub-module. This will allow them to pull in SDK changes manually at build time, or automatically through their own build scripts.

## Sub-module Installation ##

To install the SDK as a sub-module, navigate to your project repo directory and then enter the following command:

```
git submodule add https://github.com/electricimp/BlinkUpSDK-Android.git
```

This will add the SDK repo as a sub-module referenced through the file `.gitmodules`, which will be added if it is not present. You can now push your local repo to its origin:

```
git push origin <working_branch>
```

To update the sub-module contents at any time, eg. in a build script, call:

```
git submodule update --remote
```

**Note** The BlinkUp SDK repo is considered solely for consumption by a customer’s project. We will not be accepting pull requests made to the BlinkUp SDK repo. If you have support or related questions concerning the SDK, please submit them through your customer account’s [Electric Imp Support access](https://support.electricimp.com/).

## BlinkUp SDK Installation ##

To understand how to add the BlinkUp SDK files to your app project, please see the relevant documentation included with each of the SDK:

- [Android SDK Installation](./android/README.md)

## Release Notes and Known Issues ##

Please see the [Dev Center SDK page](https://developer.electricimp.com/manufacturing/sdkdocs), or the SDK documentation in this repo.

---

The BlinkUp SDK is copyright &copy; 2020, Twilio, Inc.
