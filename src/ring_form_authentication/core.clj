(ns ring-form-authentication.core)


(use 'ring.middleware.params)
(use 'ring.middleware.session)

(defn render-form [username error]
  (str
    (if error
      (str "<div class=\"error\">" error "</div>")
      "") 
    "<form id='login_form' method='post' >\n"
    "<fieldset class='main_fieldset'>\n"
    "<div><label class='label' for='username'>Username: </label><input type='text' name='username' id='username' value='"
    (if username username "" )
    "' /></div>\n"
    "<div><label class='label' for='password'>Password: </label><input type='password' name='password' id='password' value='' /></div>\n" 
    "<div><label class='label' for='remember'>Remember: </label><input type='checkbox' name='remember' id='remember' value='1' /></div>\n"
    "<div><input type='submit' name='submit' id='submit' value='Login' /></div>\n"
    "</fieldset>\n"
    "</form>"))

(defn default-html-wrapper [body]
  (str
    "<html><body>"
    body
    "</body></html>"))

(defn login-page [request authenticator html-wrapper]
  (if (= (get request :request-method) :post )
    (let [auth-result 
      (let [params (get request :params)]
        (authenticator (get params "username") (get params "password")))]
      (if (get auth-result :userid)
        (let [redir-to (or 
                         (get auth-result :redir-to)
                         (get (get request :session) :redir-to)
                         "/")]
          {:status 302
           :headers {"location" redir-to
                     "debug" (str (get request :session))}
           :session {:userid (get auth-result :userid)}})
        {:status 200
         :headers {"Content-Type" "text/html"}
         :body (html-wrapper
                 (render-form 
                   (get (get request :params) "username")
                   (str (get request :params) (get auth-result :error))))}))
    (let [redir-to (or
                     (get (get request :session) :redir-to)
                     (or 
                       (get (get request :headers) "http_referer")
                       (get (get request :headers) "referer"))
                     "/")]
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body (html-wrapper (str (render-form nil nil) "After login: " redir-to ))
       :session {:redir-to redir-to}})))
 
(defn logout-page [request html-wrapper]
  (if (= (get request :request-method) :post )
    {:status 302
     :headers {"location" "/"}
     :session {}}
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (html-wrapper "You can logout only with a POST request")}))

(defn wrap-login-page
  ([handler authenticator] (wrap-login-page handler authenticator default-html-wrapper))
  ([handler authenticator html-wrapper]
   (wrap-session
     (wrap-params 
       (fn [request] 
         (if (= (get request :uri) "/login" )
           (login-page request authenticator html-wrapper)
           (let [response (handler request)]
             (if (= (get request :uri) "/logout" )
               (logout-page request html-wrapper)
               (if (get response :session)
                 response
                 (let [session (get request :session)]
                   (assoc response :session (assoc session "mismatched-sessions-fix" "1"))))))))))))

