package org.cartagena.tool.core

import org.cartagena.tool.core.model.Context
import org.mockito.Mockito.{verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

import scala.reflect.runtime.universe.typeTag
import scala.util.{Failure, Success}

class ContextOperationsBuilderTest extends FlatSpec with Matchers with MockitoSugar {

  "get" should "return an entry value associated with a given key" in {
    // given
    val key = "key"
    val value = "value"

    val context = mock[Context]

    when(context.get(key)(typeTag[String]))
      .thenReturn(Success(value))

    // when
    val actual = ContextOperationsBuilder[String]()
      .withContext(context)
      .withKey(key)
      .get

    // then
    actual should be(value)

    verify(context).get(key)(typeTag[String])
  }

  it should "throw an exception" in {
    // given
    val key = "key"

    val context = mock[Context]

    when(context.get(key)(typeTag[String]))
      .thenReturn(Failure(new Exception))

    intercept[Exception] {
      // when
      ContextOperationsBuilder[String]()
        .withContext(context)
        .withKey(key)
        .get
    }

    // then
    verify(context).get(key)(typeTag[String])
  }

  "create" should "create an entry with given value and key" in {
    // given
    val key = "key"
    val value = "value"

    val context = mock[Context]

    when(context.create(key, value)(typeTag[String]))
      .thenReturn(Success(value))

    // when
    val actual = ContextOperationsBuilder[String]()
      .withContext(context)
      .withKey(key)
      .withValue(value)
      .create()

    // then
    actual should be(value)

    verify(context).create(key, value)(typeTag[String])
  }

  "update" should "update an entry value associated with a given key" in {
    // given
    val key = "key"
    val value = "value"

    val context = mock[Context]

    when(context.update(key, value)(typeTag[String]))
      .thenReturn(Success(value))

    // when
    val actual = ContextOperationsBuilder[String]()
      .withContext(context)
      .withKey(key)
      .withValue(value)
      .update()

    // then
    actual should be(value)

    verify(context).update(key, value)(typeTag[String])
  }

  "remove" should "remove an entry associated with a given key" in {
    // given
    val key = "key"
    val value = "value"

    val context = mock[Context]

    when(context.remove(key)(typeTag[String]))
      .thenReturn(Success(value))

    // when
    val actual = ContextOperationsBuilder[String]()
      .withContext(context)
      .withKey(key)
      .remove()

    // then
    actual should be(value)

    verify(context).remove(key)(typeTag[String])
  }

}
