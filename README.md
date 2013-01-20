# ring-form-authentication

A Clojure library designed to rapid development/scaffolding of form based authentication.

It is a Ring based middleware.

It adds a "/login" and "/logout" pages, redirects after a succesful authentication
and manages user data in the session.

This is very EXPERIMENTAL - it is my first code in Clojure.

## Usage

(use 'ring-form-authentication.core)

(def app
  (wrap-login-page original-app password-checker))

## License

Copyright © 2013 Zbigniew Łukasiak zzbbyy@gmail.com

Distributed under the Eclipse Public License, the same as Clojure.
