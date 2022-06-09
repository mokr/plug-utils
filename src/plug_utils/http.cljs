(ns plug-utils.http)


(defn client-redirect
  "Frontend redirect to provided url, or to the login screen if no url given."
  ([] (client-redirect "/login"))
  ([url] (set! (.-location js/document) url)))


(defn auth-issue?
  "Response on HTTP request indicates forbidden (=> user is not authenticated)"
  [{:keys [last-error-code status] :as error-response}]
  (and (= status 403)
       (= last-error-code 6)))