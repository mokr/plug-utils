(ns plug-utils.re-frame
  "Re-frame related utils that are typically used or required by every project using plug-*"
  (:require [plug-utils.http :as http]
            [clojure.spec.alpha :as s]
            [plug-utils.spec :refer [valid?]]
            [plug-utils.specs.re-frame :as $]
            [re-frame.core :as rf]
            [taoensso.timbre :as log]))

;;MAYBE: Common init event. Utilize e.g. :js-vars cofx below to get JS vars into DB.


;|-------------------------------------------------
;| SUGAR

(def <sub (comp deref rf/subscribe))
(def >evt rf/dispatch)


;|-------------------------------------------------
;| EVENTS

;; Note: plug-fetch dispatches this with keys [:source :action :message :raw]
(rf/reg-event-fx
  :reg/error
  [rf/trim-v]
  (fn [{:keys [db]} [{:keys [source action message raw] :as error}]]
    {:pre  [(valid? ::$/error-map error)]
     :post [(map? %)]}
    ;(log/debug "error" error)
    (let [{:keys [last-error status-text debug-message]} raw
          message           (or message last-error status-text debug-message)
          ;notification-text    (str action " => " message)
          notification-text message]
      (if (http/auth-issue? raw)
        {:redirect-to "/login"}
        {:dispatch [:new/notification {:severity :error
                                       :text     notification-text}]
         :log      {:severity :error
                    :text     (str "ERR: " source " :: " action " => " message)}
         :db       (assoc db :last/error error)}))))


;|-------------------------------------------------
;| COFX / COEFFECTS

;; Creates conflicts and expectation of having given js vars available. Better to implement in each project
;(rf/reg-cofx
;  :js-vars
;  (fn [coeffects]
;    (assoc coeffects :js-vars {:user/identity js/identity
;                               :user/role     js/role})))


;|-------------------------------------------------
;| FX / EFFECTS

;; Example chain of events: plug-fetch fails du to 403 => redirect to /login provided by plug-sso
(rf/reg-fx
  :redirect-to
  (fn [url]
    (http/client-redirect url)))


(rf/reg-fx
  :log
  (fn [{:keys [severity text]}]
    (case severity
      :error (js/console.error text)
      :warn (js/console.warn text)
      :info (js/console.info text)
      (js/console.log text))))
