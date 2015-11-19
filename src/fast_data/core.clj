(ns fast-data.core
  (:use fast-data.data)
  (:require [taoensso.nippy :as nippy])
  (:require [cognitect.transit :as transit])
  (:require [criterium.core :refer :all])
  (:require [clojure.edn :as edn])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream])
  (:gen-class))

(set! *warn-on-reflection* true)


(defn edn-roundtrip [data]
  (edn/read-string (pr-str data)))


(defn nippy-roundtrip [data]
  (nippy/thaw (nippy/freeze data)))


(defn transit-roundtrip [data]
  (let [out    (ByteArrayOutputStream. 1024) ;; initial size
        writer #(do (transit/write (transit/writer out :json) %) out)
        reader (fn [^ByteArrayOutputStream baos]
                 (transit/read
                  (transit/reader (ByteArrayInputStream. (.toByteArray baos)) :json)))]
    (reader (writer data))))




(defn headline [line]
  (let [strf (fn [size strn] (apply str (repeat size strn)))
        bar  (strf 80 "-")]
    (when line
      (println bar)
      (println (strf 10 " ") "Benchmark:" (pr-str line)))
    (println bar "\n")))



(defmacro run-bench [& body]
  `(do
     (headline '~@body)
     (quick-bench
      ~@body)
     (headline nil)))



(defn verify-roundtrip [f d]
  (= d (f d)))

(comment
  (for [data  ["hello" basic-data users-data]
      func  [nippy-roundtrip transit-roundtrip edn-roundtrip]]
    (try
      (verify-roundtrip func data)
      (catch Exception x
        false)))


  (verify-roundtrip edn-roundtrip basic-data )

  (pr-str basic-data)
  (let [data users-data] (= data (transit-roundtrip data)))

  (type (edn/read-string (pr-str (byte 10)))))


(defmacro run-all-bench [data]
  `(do
     (try
       (run-bench (edn-roundtrip   ~data))
       (catch Exception x#))
     (run-bench (transit-roundtrip ~data))
     (run-bench (nippy-roundtrip   ~data))))


(defn -main []
  (run-all-bench (byte 1))
  (run-all-bench basic-data)
  (run-all-bench users-data))
