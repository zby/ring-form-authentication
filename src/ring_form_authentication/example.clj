(ns ring-form-authentication.example)
(use 'ring-form-authentication.core)

(defn our-handler [request]
  (let [body (str
               "<html><body>"
               "This is example app.\n"
               (if (get (get request :session) :userid )
                 (str 
                   "The user is logged in: " (get (get request :session) :userid ) "<br/>"
                   "<form id='logout_form' name='logout_form' method='post' action='/logout'>"
                   "<input type='submit' name='submit' id='submit' value='Logout' />"
                   "</form>")
                 "<a href='/login'>login</a>")
               "<br/>\nSession data: " (get request :session)
               "</body></html>")]
    {:status 200
     :headers {"Content-Type" "text/html" "debug" (str (get request :session))}
     :body body}))

(def handler
  (wrap-login-page our-handler 
                    (fn [username password] 
                      (if (and username (= username password)) 
                        {:userid username } 
                        {:error (str "Wrong username or password: " username " " password)}))))

