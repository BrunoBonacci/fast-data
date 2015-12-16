(defproject fast-data "0.1.0-SNAPSHOT"
  :description "Benchmarks for various solution for marshalling/unmarshalling of Clojure data structures"
  :url "https://github.com/BrunoBonacci/fast-data.git"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.taoensso/nippy "2.10.0"]
                 [com.cognitect/transit-clj "0.8.285"]
                 [org.clojure/data.fressian "0.2.1"]
                 [criterium "0.4.3"]]

  :main fast-data.core
  :jvm-opts ["-server" "-Dfile.encoding=utf-8"])
