(defproject net.clojars.mokr/plug-utils "0.1.1-SNAPSHOT"
  :description "Small util functions for clj and/or cljs"
  :url "https://github.com/mokr/plug-utils"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[clj-commons/clj-yaml "0.7.108"]
                 [org.clojure/clojure "1.11.1" :scope "provided"]
                 [org.clojure/clojurescript "1.11.54" :scope "provided"]
                 [org.clojure/core.async "1.5.648" :scope "provided"]
                 [com.taoensso/timbre "5.2.1"]]
  :repl-options {:init-ns plug-utils.re-frame}

  :profiles
  {:cljs
   {:source-paths ["src" "test"]
    :dependencies [[re-frame "1.2.0" :scope "provided"]
                   [reagent "1.1.1" :scope "provided"]
                   [thheller/shadow-cljs "2.19.2" :scope "provided"]]}})
