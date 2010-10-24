;   Copyright (c) Andrew Boekhoff. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns jsasm.ast)

(defmacro deftoken [tag]
  `(defn ~tag [& vals#]
     (->> vals#
          (cons ~(keyword tag))
          vec)))

(defmacro deftokens [& tags]
  `(do ~@(for [t tags] `(deftoken ~t))))

(deftokens
  LIT
  REGEX
  ARRAY
  OBJECT
  ENTRY
  FUNCTION
  PROJECT
  CALL
  IF
  WHILE
  SET!
  OPERATOR
  BREAK
  RETURN
  VAR
  NEW
  TRY
  TRY*
  THROW
  CATCH
  FINALLY
  ENUM
  COMMENT)



