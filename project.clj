(defproject org.clojars.vladimirmarkovic86/language-lib "0.2.6"
  :description "Language library"
  :url "http://github.com/VladimirMarkovic86/language-lib"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojars.vladimirmarkovic86/mongo-lib "0.2.3"]
                 [org.clojars.vladimirmarkovic86/dao-lib "0.3.3"]
                 [org.clojars.vladimirmarkovic86/session-lib "0.2.6"]
                 [org.clojure/clojurescript "1.10.339"]
                 [org.clojars.vladimirmarkovic86/ajax-lib "0.1.2"]
                 [org.clojars.vladimirmarkovic86/common-middle "0.2.2"]
                 ]

  :min-lein-version "2.0.0"
  
  :source-paths ["src/clj" "src/cljs"])

