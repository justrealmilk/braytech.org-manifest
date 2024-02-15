import BraytechActivityDefinition from '@/src/data/manifest/en-OwO/BraytechActivityDefinition.json';
import BraytechActivityDifficultyDefinition from '@/src/data/manifest/en-OwO/BraytechActivityDifficultyDefinition.json';
import BraytechCommonDefinition from '@/src/data/manifest/en-OwO/BraytechCommonDefinition.json';
import BraytechFeatureDefinition from '@/src/data/manifest/en-OwO/BraytechFeatureDefinition.json';
import BraytechMapsDefinition from '@/src/data/manifest/en-OwO/BraytechMapsDefinition.json';
import BraytechMapsTypesDefinition from '@/src/data/manifest/en-OwO/BraytechMapsTypesDefinition.json';
import BraytechRotationDefinition from '@/src/data/manifest/en-OwO/BraytechRotationDefinition.json';
import DestinyActivityDefinition from '@/src/data/manifest/en-OwO/DestinyActivityDefinition.json';
import DestinyActivityModeDefinition from '@/src/data/manifest/en-OwO/DestinyActivityModeDefinition.json';
import DestinyActivityModifierDefinition from '@/src/data/manifest/en-OwO/DestinyActivityModifierDefinition.json';
import DestinyActivityTypeDefinition from '@/src/data/manifest/en-OwO/DestinyActivityTypeDefinition.json';
import DestinyChecklistDefinition from '@/src/data/manifest/en-OwO/DestinyChecklistDefinition.json';
import DestinyCollectibleDefinition from '@/src/data/manifest/en-OwO/DestinyCollectibleDefinition.json';
import DestinyDamageTypeDefinition from '@/src/data/manifest/en-OwO/DestinyDamageTypeDefinition.json';
import DestinyDestinationDefinition from '@/src/data/manifest/en-OwO/DestinyDestinationDefinition.json';
import DestinyHistoricalStatsDefinition from '@/src/data/manifest/en-OwO/DestinyHistoricalStatsDefinition.json';
import DestinyItemSubTypeDefinition from '@/src/data/manifest/en-OwO/DestinyItemSubTypeDefinition.json';
import DestinyItemTypeDefinition from '@/src/data/manifest/en-OwO/DestinyItemTypeDefinition.json';
import DestinyInventoryBucketDefinition from '@/src/data/manifest/en-OwO/DestinyInventoryBucketDefinition.json';
import DestinyInventoryItemDefinition from '@/src/data/manifest/en-OwO/DestinyInventoryItemDefinition.json';
import DestinyMilestoneDefinition from '@/src/data/manifest/en-OwO/DestinyMilestoneDefinition.json';
import DestinyObjectiveDefinition from '@/src/data/manifest/en-OwO/DestinyObjectiveDefinition.json';
import DestinyPhaseDefinition from '@/src/data/manifest/en-OwO/DestinyPhaseDefinition.json';
import DestinyPresentationNodeDefinition from '@/src/data/manifest/en-OwO/DestinyPresentationNodeDefinition.json';
import DestinyRecordDefinition from '@/src/data/manifest/en-OwO/DestinyRecordDefinition.json';
import DestinySeasonDefinition from '@/src/data/manifest/en-OwO/DestinySeasonDefinition.json';
import DestinySourceDefinition from '@/src/data/manifest/en-OwO/DestinySourceDefinition.json';
import DestinyStatDefinition from '@/src/data/manifest/en-OwO/DestinyStatDefinition.json';
import DestinyTraitDefinition from '@/src/data/manifest/en-OwO/DestinyTraitDefinition.json';

import DestinyCollectibleDefinitionColloquial from '@/src/data/manifest/en-OwO/DestinyCollectibleDefinitionColloquial.json';
import DestinyInventoryItemDefinitionColloquial from '@/src/data/manifest/en-OwO/DestinyInventoryItemDefinitionColloquial.json';

const enOwO = {
  definitions: {
    BraytechActivityDefinition,
    BraytechActivityDifficultyDefinition,
    BraytechCommonDefinition,
    BraytechFeatureDefinition,
    BraytechMapsDefinition,
    BraytechMapsTypesDefinition,
    BraytechRotationDefinition,
    DestinyActivityDefinition,
    DestinyActivityModeDefinition,
    DestinyActivityModifierDefinition,
    DestinyActivityTypeDefinition,
    DestinyChecklistDefinition,
    DestinyCollectibleDefinition,
    DestinyDamageTypeDefinition,
    DestinyDestinationDefinition,
    DestinyHistoricalStatsDefinition,
    DestinyItemSubTypeDefinition,
    DestinyItemTypeDefinition,
    DestinyInventoryBucketDefinition,
    DestinyInventoryItemDefinition,
    DestinyMilestoneDefinition,
    DestinyObjectiveDefinition,
    DestinyPhaseDefinition,
    DestinyPresentationNodeDefinition,
    DestinyRecordDefinition,
    DestinySeasonDefinition,
    DestinySourceDefinition,
    DestinyStatDefinition,
    DestinyTraitDefinition,
  },
  optional: {
    colloquialDefinitions: {
      DestinyCollectibleDefinition: DestinyCollectibleDefinitionColloquial,
      DestinyInventoryItemDefinition: DestinyInventoryItemDefinitionColloquial,
    },
  },
};

export default enOwO;
