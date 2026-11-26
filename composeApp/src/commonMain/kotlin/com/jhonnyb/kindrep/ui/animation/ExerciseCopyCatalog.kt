package com.jhonnyb.kindrep.ui.animation

import androidx.compose.runtime.Immutable
import com.jhonnyb.kindrep.domain.model.ExercisePhase
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_setup
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_title
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_setup
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_title
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_setup
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_title
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_setup
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_title
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_setup
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_title
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_setup
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_title
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_setup
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_title
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_setup
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_title
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_setup
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_title
import org.jetbrains.compose.resources.StringResource

@Immutable
data class ExerciseCopyResources(
    val title: StringResource,
    val steadyFormCue: StringResource,
    val setupInstruction: StringResource,
    val breathingCue: StringResource,
    val phaseExtendLabel: StringResource,
    val phaseHoldLabel: StringResource,
    val phaseReturnLabel: StringResource,
) {
    fun phaseLabel(phase: ExercisePhase): StringResource = when (phase) {
        ExercisePhase.EXTEND -> phaseExtendLabel
        ExercisePhase.HOLD -> phaseHoldLabel
        ExercisePhase.RETURN -> phaseReturnLabel
    }
}

object ExerciseCopyCatalog {
    private val copyByMediaKey: Map<String, ExerciseCopyResources> = mapOf(
        ExerciseMediaKeys.CAT_COW to ExerciseCopyResources(
            title = Res.string.exercise_cat_cow_title,
            steadyFormCue = Res.string.exercise_cat_cow_steady_cue,
            setupInstruction = Res.string.exercise_cat_cow_setup,
            breathingCue = Res.string.exercise_cat_cow_breathing,
            phaseExtendLabel = Res.string.exercise_cat_cow_phase_extend,
            phaseHoldLabel = Res.string.exercise_cat_cow_phase_hold,
            phaseReturnLabel = Res.string.exercise_cat_cow_phase_return,
        ),
        ExerciseMediaKeys.BIRD_DOG to ExerciseCopyResources(
            title = Res.string.exercise_bird_dog_title,
            steadyFormCue = Res.string.exercise_bird_dog_steady_cue,
            setupInstruction = Res.string.exercise_bird_dog_setup,
            breathingCue = Res.string.exercise_bird_dog_breathing,
            phaseExtendLabel = Res.string.exercise_bird_dog_phase_extend,
            phaseHoldLabel = Res.string.exercise_bird_dog_phase_hold,
            phaseReturnLabel = Res.string.exercise_bird_dog_phase_return,
        ),
        ExerciseMediaKeys.GLUTE_BRIDGE to ExerciseCopyResources(
            title = Res.string.exercise_glute_bridge_title,
            steadyFormCue = Res.string.exercise_glute_bridge_steady_cue,
            setupInstruction = Res.string.exercise_glute_bridge_setup,
            breathingCue = Res.string.exercise_glute_bridge_breathing,
            phaseExtendLabel = Res.string.exercise_glute_bridge_phase_extend,
            phaseHoldLabel = Res.string.exercise_glute_bridge_phase_hold,
            phaseReturnLabel = Res.string.exercise_glute_bridge_phase_return,
        ),
        ExerciseMediaKeys.DEAD_BUG to ExerciseCopyResources(
            title = Res.string.exercise_dead_bug_title,
            steadyFormCue = Res.string.exercise_dead_bug_steady_cue,
            setupInstruction = Res.string.exercise_dead_bug_setup,
            breathingCue = Res.string.exercise_dead_bug_breathing,
            phaseExtendLabel = Res.string.exercise_dead_bug_phase_extend,
            phaseHoldLabel = Res.string.exercise_dead_bug_phase_hold,
            phaseReturnLabel = Res.string.exercise_dead_bug_phase_return,
        ),
        ExerciseMediaKeys.CHILDS_POSE to ExerciseCopyResources(
            title = Res.string.exercise_childs_pose_title,
            steadyFormCue = Res.string.exercise_childs_pose_steady_cue,
            setupInstruction = Res.string.exercise_childs_pose_setup,
            breathingCue = Res.string.exercise_childs_pose_breathing,
            phaseExtendLabel = Res.string.exercise_childs_pose_phase_extend,
            phaseHoldLabel = Res.string.exercise_childs_pose_phase_hold,
            phaseReturnLabel = Res.string.exercise_childs_pose_phase_return,
        ),
        ExerciseMediaKeys.CLAMSHELL to ExerciseCopyResources(
            title = Res.string.exercise_clamshell_title,
            steadyFormCue = Res.string.exercise_clamshell_steady_cue,
            setupInstruction = Res.string.exercise_clamshell_setup,
            breathingCue = Res.string.exercise_clamshell_breathing,
            phaseExtendLabel = Res.string.exercise_clamshell_phase_extend,
            phaseHoldLabel = Res.string.exercise_clamshell_phase_hold,
            phaseReturnLabel = Res.string.exercise_clamshell_phase_return,
        ),
        ExerciseMediaKeys.WALL_SLIDE to ExerciseCopyResources(
            title = Res.string.exercise_wall_slide_title,
            steadyFormCue = Res.string.exercise_wall_slide_steady_cue,
            setupInstruction = Res.string.exercise_wall_slide_setup,
            breathingCue = Res.string.exercise_wall_slide_breathing,
            phaseExtendLabel = Res.string.exercise_wall_slide_phase_extend,
            phaseHoldLabel = Res.string.exercise_wall_slide_phase_hold,
            phaseReturnLabel = Res.string.exercise_wall_slide_phase_return,
        ),
        ExerciseMediaKeys.CHIN_TUCK to ExerciseCopyResources(
            title = Res.string.exercise_chin_tuck_title,
            steadyFormCue = Res.string.exercise_chin_tuck_steady_cue,
            setupInstruction = Res.string.exercise_chin_tuck_setup,
            breathingCue = Res.string.exercise_chin_tuck_breathing,
            phaseExtendLabel = Res.string.exercise_chin_tuck_phase_extend,
            phaseHoldLabel = Res.string.exercise_chin_tuck_phase_hold,
            phaseReturnLabel = Res.string.exercise_chin_tuck_phase_return,
        ),
        ExerciseMediaKeys.BULGARIAN_SPLIT_SQUAT to ExerciseCopyResources(
            title = Res.string.exercise_bulgarian_split_squat_title,
            steadyFormCue = Res.string.exercise_bulgarian_split_squat_steady_cue,
            setupInstruction = Res.string.exercise_bulgarian_split_squat_setup,
            breathingCue = Res.string.exercise_bulgarian_split_squat_breathing,
            phaseExtendLabel = Res.string.exercise_bulgarian_split_squat_phase_extend,
            phaseHoldLabel = Res.string.exercise_bulgarian_split_squat_phase_hold,
            phaseReturnLabel = Res.string.exercise_bulgarian_split_squat_phase_return,
        ),
    )

    fun resolve(mediaKey: String): ExerciseCopyResources =
        copyByMediaKey[mediaKey] ?: copyByMediaKey.getValue(ExerciseMediaKeys.BIRD_DOG)
}
