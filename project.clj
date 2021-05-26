(defproject net.clojars.mokr/plug-utils "0.1.0-SNAPSHOT"
  :description "Small util functions for clj and/or cljs"
  :url "https://github.com/mokr/plug-utils"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3" :scope "provided"]
                 [org.clojure/clojurescript "1.10.844" :scope "provided"]
                 [org.clojure/core.async "1.3.618" :scope "provided"]
                 [re-frame "1.2.0" :scope "provided"]
                 [reagent "1.0.0" :scope "provided"]]
  :repl-options {:init-ns plug-utils.re-frame})
