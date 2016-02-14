package com.typesafe.sbt

import java.io.File

import org.apache.maven.repository.internal._
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.transport.wagon.WagonProvider
import org.eclipse.aether.{RepositorySystem, RepositorySystemSession}

/** Helper methods for dealing with starting up Aether. */
package object pom {
  def newRepositorySystemImpl: RepositorySystem = {
    val locator = MavenRepositorySystemUtils.newServiceLocator()
    locator.setServices(classOf[WagonProvider], new HackedWagonProvider)
    locator.getService(classOf[RepositorySystem])
  }

  def newSessionImpl(system: RepositorySystem, localRepoDir: File): RepositorySystemSession = {
    val session = MavenRepositorySystemUtils.newSession()
    val localRepo: LocalRepository = new LocalRepository(localRepoDir.getAbsolutePath)
    session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo))
    session
  }

  def defaultLocalRepo: java.io.File = {
    import sbt._
    file(sys.props("user.home")) / ".m2" / "repository"
  }

  def loadEffectivePom(pom: File, localRepo: File = defaultLocalRepo, profiles: Seq[String], userProps: Map[String, String]) =
    MavenPomResolver(localRepo).loadEffectivePom(pom, Seq.empty, profiles, userProps)
}
