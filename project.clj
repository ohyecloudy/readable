(defproject readable "0.1.0-SNAPSHOT"
  :description "test page readability"
  :url "https://github.com/ohyecloudy/readable"
  :license {:name "The MIT License (MIT)"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [prismatic/dommy "0.1.1"]]
  :plugins [[lein-cljsbuild "0.3.2"]]
  :cljsbuild {:builds [{:source-paths ["src-cljs"]
                        :compiler {:output-to "js/main.js"
                                   :optimizations
                                   :whitespace
                                   :pretty-print true}}]})
