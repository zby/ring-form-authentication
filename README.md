# ring-form-authentication

A Clojure library designed to rapid development/scaffolding of form based authentication.
It lets you quickly add to your application "/login" and "/logout" pages
pluss all the redirects and session management needed.

It is a Ring based middleware.

After the user has authenticated the session keeps his userid and the
application can check it for authorization:
(if (get (get request :session) :userid )

See example application in src/ring_form_authentication/example.clj

This is very EXPERIMENTAL - it is my first code in Clojure.

## Usage

(use 'ring-form-authentication.core)

(def app
  (wrap-login-page original-app password-checker))

## License

Copyright © 2013 Zbigniew Łukasiak zzbbyy@gmail.com

Distributed under the Eclipse Public License, the same as Clojure.
