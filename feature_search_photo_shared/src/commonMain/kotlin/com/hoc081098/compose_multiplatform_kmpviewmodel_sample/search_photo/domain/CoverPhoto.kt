package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain

import kotlinx.datetime.Instant

data class CoverPhoto(
  val id: String,
  val slug: String,
  val createdAt: Instant,
  val updatedAt: Instant,
  val promotedAt: Instant?,
  val width: Int,
  val height: Int,
)