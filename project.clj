(defproject org.clojars.vladimirmarkovic86/language-lib "0.2.30"
  :description "Language library"
  :url "http://github.com/VladimirMarkovic86/language-lib"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojars.vladimirmarkovic86/mongo-lib "0.2.8"]
                 [org.clojars.vladimirmarkovic86/dao-lib "0.3.22"]
                 [org.clojars.vladimirmarkovic86/session-lib "0.2.22"]
                 [org.clojure/clojurescript "1.10.339"]
                 [org.clojars.vladimirmarkovic86/ajax-lib "0.1.11"]
                 [org.clojars.vladimirmarkovic86/common-middle "0.2.8"]
                 ]

  :min-lein-version "2.0.0"
  
  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj"]

  :plugins [[lein-cljsbuild  "1.1.7"]
            [lein-doo "0.1.11"]
            ]

  :cljsbuild
    {:builds
      {:test
        {:source-paths ["src/cljs" "test/cljs"]
         :compiler     {:main language-lib.test-runner
                        :optimizations :whitespace
                        :output-dir "resources/public/assets/js/out/test"
                        :output-to "resources/public/assets/js/test.js"}}
       }}
 )

