package outwatch.dom

import cats.Applicative
import outwatch.dom.helpers._
import cats.effect.Effect

/** Trait containing the contents of the `Attributes` module, so they can be
  * mixed in to other objects if needed. This should contain "all" attributes
  * and mix in other traits (defined above) as needed to get full coverage.
  */
trait OutwatchAttributes
  extends SnabbdomKeyAttributes
  with OutWatchLifeCycleAttributes

/** Outwatch component life cycle hooks. */
trait OutWatchLifeCycleAttributes {
  /**
    * Lifecycle hook for component insertion.
    *
    * This hook is invoked once the DOM element for a vnode has been inserted into the document
    * and the rest of the patch cycle is done.
    */
  lazy val onInsert   = SimpleEmitterBuilder(InsertHook)

  /** Lifecycle hook for component prepatch. */
  lazy val onPrePatch   = SimpleEmitterBuilder(PrePatchHook)

  /** Lifecycle hook for component updates. */
  lazy val onUpdate   = SimpleEmitterBuilder(UpdateHook)

  /**
    * Lifecycle hook for component postpatch.
    *
    *  This hook is invoked every time a node has been patched against an older instance of itself.
    */
  lazy val onPostPatch   = SimpleEmitterBuilder(PostPatchHook)

  /**
    * Lifecycle hook for component destruction.
    *
    * This hook is invoked on a virtual node when its DOM element is removed from the DOM
    * or if its parent is being removed from the DOM.
    */
  lazy val onDestroy  = SimpleEmitterBuilder(DestroyHook)
}

/** Snabbdom Key Attribute */
trait SnabbdomKeyAttributes {
  lazy val key = KeyBuilder
}

trait AttributeHelpers[F[+_]] { self: Attributes[F] =>
  lazy val `class` = className

  lazy val `for` = forId

  lazy val data = new DynamicAttrBuilder[F, Any]("data" :: Nil)

  def attr[T](key: String, convert: T => Attr.Value = (t: T) => t.toString : Attr.Value) =
    new BasicAttrBuilder[F, T](key, convert)
  def prop[T](key: String, convert: T => Prop.Value = (t: T) => t) = new PropBuilder[F, T](key, convert)
  def style[T](key: String) = new BasicStyleBuilder[F, T](key)
}

trait TagHelpers[F[+_]] {
  def tag(name: String)(implicit F: Effect[F]): VNodeF[F] = Applicative[F].pure(VTree[F](name, Seq.empty))
}